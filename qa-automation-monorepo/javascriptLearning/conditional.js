let age = 18;
if (age >= 18) {
    console.log("You are an adult.");
} else {
    console.log("You are a minor.");
}

let score = 85;
if (score >= 90) {
    console.log("Grade: A");
} else if (score >= 80) {
    console.log("Grade: B");
} else if (score >= 70) {
    console.log("Grade: C");
} else if (score >= 60) {
    console.log("Grade: D");
} else {
    console.log("Grade: F");
}

let day = "Monday";
switch (day) {
    case "Monday":
        console.log("Start of the week.");
        break;
    case "Tuesday":
        console.log("Second day of the week.");
        break;
    default:
        console.log("It's a different day.");
}

if(!true){
    console.log("This will not be executed.");
}
else{
    console.log("This will be executed.");
}