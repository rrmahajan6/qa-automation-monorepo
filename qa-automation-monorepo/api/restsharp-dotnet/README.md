# RestSharpProject

A minimal .NET console app that uses RestSharp to call a public REST API.

## Prerequisites

- .NET SDK (9+ or 10)

## Run

```bash
dotnet restore
dotnet run
```

## What it does

- Sends a `GET` request to `https://jsonplaceholder.typicode.com/posts/1`
- Prints HTTP status code and response body
