export function addTwoNumbers(a,b){
    return a+b;
}
//set operation
export function arrays(){
    let arr1 = [1,2,3,4,1,2,3,4];
    for(let i=0; i<arr1.length;i++){
        console.log(arr1[i]);
    }
}

export function objectExample(){
    let obj = {
        1: 'one',
        2: 'two',
        3: 'three'
    };
    for(let key in obj){
        console.log(key);
        console.log(obj[key]);
    }
}

export function setExample(){
    let set = new Set();
    set.add(1);
    set.add(2);
    set.add(3);
    set.add(1); // duplicate value will not be added
    console.log(set);
    set.forEach(value => {
        console.log(value);
    });
}

export function mapExample(){
    let map = new Map();
    map.set('name', 'John');
    map.set('age', 30);
    map.set('city', 'New York');
    console.log(map);
    map.forEach((value, key) => {
        console.log(key + ': ' + value);
    });
}

export function reverseString(){
    let str = 'Hello World';
    for(let i=str.length-1; i>=0; i--){
        console.log(str[i]);
    }
}