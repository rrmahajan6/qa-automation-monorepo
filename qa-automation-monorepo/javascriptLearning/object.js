/**
 * what is an object?
 * An object is a collection of properties, where each property is a key-value pair. The key is a string (or symbol) that serves as an identifier for the property, and the value can be any data type, including another object or a function. Objects are used to represent real-world entities and their attributes in programming.
 * 
 * Objects can be created using object literals, constructor functions, or the Object.create() method. They can also have methods, which are functions that are associated with the object and can operate on its properties.   
 * 
 */
// Creating an object using an object literal
const person = {
    name: "John Doe",
    age: 30,
    isStudent: false,
    hobbies: ["reading", "traveling", "coding"],
    address: {
        street: "123 Main St",
        city: "Anytown",
        country: "USA"
    },
    greet: function() {
        return "Hello, my name is " + this.name;
    }
};

// Accessing object properties
console.log("Name:", person.name); // Output: John Doe
console.log("Age:", person.age); // Output: 30
console.log("Hobbies:", person.hobbies); // Output: ["reading", "traveling", "coding"]
console.log("City:", person.address.city); // Output: Anytown

// Calling a method of the object
console.log(person.greet()); // Output: Hello, my name is John Doe

// Adding a new property to the object
person.email = "john.doe@example.com";
console.log("Email:", person.email); // Output:
//for...in loop to iterate over the properties of an object
console.log("\n--- Iterating over object properties using for...in loop ---");
for (let key in person) {
    if (person.hasOwnProperty(key)) { // Check if the property is a direct property of the object
        console.log(key + ": " + person[key]);
    }
}

// Deleting a property from the object
delete person.isStudent;
console.log("Is Student:", person.isStudent); // Output: undefined

// Checking if a property exists in the object
console.log("Does 'name' property exist?", "name" in person); // Output: true
console.log("Does 'isStudent' property exist?", "isStudent" in person); // Output: false

// Object.keys() to get an array of the object's own property names
console.log("Object Keys:", Object.keys(person)); // Output: ["name", "age", "hobbies", "address", "greet", "email"]

// Object.values() to get an array of the object's own property values
console.log("Object Values:", Object.values(person)); // Output: ["John Doe", 30, ["reading", "traveling", "coding"], {street: "123 Main St", city: "Anytown", country: "USA"}, function() {...}, "
//object creation using a constructor function
function Car(make, model, year) {
    this.make = make;
    this.model = model;
    this.year = year;
    this.getCarInfo = function() {
        return this.year + " " + this.make + " " + this.model;
    };
}

const car1 = new Car("Toyota", "Camry", 2020);
console.log(car1.getCarInfo()); // Output: 2020 Toyota Camry

//assignment
const car2 = car1; // car2 is now a reference to the same object as car1
console.log(car2.getCarInfo()); // Output: 2020 Toyota Camry

// Modifying car2 will also modify car1 since they reference the same object
car2.year = 2021;
console.log(car1.getCarInfo()); // Output: 2021 Toyota Camry
console.log(car2.getCarInfo()); // Output: 2021 Toyota Camry

//changing value of a property of an object
person.name = "Jane Doe";
console.log("Updated Name:", person.name); // Output: Jane Doe

// Adding a new method to the object
person.sayGoodbye = function() {
    return "Goodbye from " + this.name;
};
console.log(person.sayGoodbye()); // Output: Goodbye from Jane Doe

//merging two objects using Object.assign()
const obj1 = { a: 1, b: 2 };
const obj2 = { b: 3, c: 4 };
const mergedObj = Object.assign({}, obj1, obj2);
console.log("Merged Object:", mergedObj); // Output: { a: 1, b: 3, c: 4 }
