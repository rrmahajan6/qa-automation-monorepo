import json
import os
import re
import requests

DATASET_FILE = os.path.join(os.path.dirname(__file__), "eval_dataset.json")
OUTPUT_FILE = os.path.join(os.path.dirname(__file__), "evaluation_results.json")


def normalize(text: str) -> str:
    return re.sub(r"\s+", " ", text.strip().lower())


def exact_match(prediction: str, expected: str) -> bool:
    return normalize(prediction) == normalize(expected)


def supported_by_context(prediction: str, context: str) -> bool:
    if not context:
        return False
    norm = normalize(prediction)
    context_norm = normalize(context)
    return norm in context_norm or any(word in context_norm for word in norm.split())


def compute_metrics(record: dict, prediction: str) -> dict:
    expected = record.get("expected_answer", "")
    context = record.get("context", "")

    accuracy = exact_match(prediction, expected)
    context_accuracy = False
    hallucination = False

    if record.get("context"):
        context_accuracy = accuracy and supported_by_context(prediction, context)
        hallucination = not supported_by_context(prediction, context)
    else:
        context_accuracy = accuracy
        hallucination = not accuracy

    return {
        "prediction": prediction,
        "accuracy": accuracy,
        "context_accuracy": context_accuracy,
        "hallucination": hallucination,
    }


def build_prompt(record: dict) -> str:
    prompt = "Use the supplied context to answer the question accurately."
    if record.get("context"):
        prompt += f"\n\nContext:\n{record['context']}"
    prompt += f"\n\nQuestion:\n{record['prompt']}"
    prompt += "\n\nAnswer concisely and only with supported information."
    return prompt


def query_model(prompt: str) -> str:
    """Query the local Ollama model."""
    try:
        response = requests.post(
            "http://localhost:11434/api/generate",
            json={"model": "llama3.2", "prompt": prompt, "stream": False},
            timeout=60
        )
        response.raise_for_status()
        return response.json()["response"].strip()
    except requests.RequestException as e:
        raise RuntimeError(f"Error querying Ollama: {e}")


def load_dataset(path: str) -> list[dict]:
    with open(path, "r", encoding="utf-8") as f:
        return json.load(f)


def save_results(results: list[dict]) -> None:
    with open(OUTPUT_FILE, "w", encoding="utf-8") as f:
        json.dump(results, f, indent=2)
    print(f"Saved evaluation results to: {OUTPUT_FILE}")


def run_evaluation() -> None:
    print("Loading dataset from:", DATASET_FILE)
    dataset = load_dataset(DATASET_FILE)
    results = []

    for record in dataset:
        prompt = build_prompt(record)
        prediction = query_model(prompt)

        metrics = compute_metrics(record, prediction)
        metrics.update(
            id=record["id"],
            prompt=record["prompt"],
            context=record.get("context", ""),
            expected_answer=record.get("expected_answer", ""),
        )
        results.append(metrics)

        print(f"[{record['id']}] prediction: {prediction}")
        print(f"  accuracy={metrics['accuracy']}, context_accuracy={metrics['context_accuracy']}, hallucination={metrics['hallucination']}\n")

    save_results(results)

    total = len(results)
    accuracy_rate = sum(1 for r in results if r["accuracy"]) / total
    context_rate = sum(1 for r in results if r["context_accuracy"]) / total
    hallucination_rate = sum(1 for r in results if r["hallucination"]) / total

    print("Final metrics:")
    print(f"  accuracy: {accuracy_rate:.2%}")
    print(f"  context_accuracy: {context_rate:.2%}")
    print(f"  hallucination_rate: {hallucination_rate:.2%}")


if __name__ == "__main__":
    run_evaluation()
