var readline = require('readline-sync');
var start,finish;
function incrementor() {
{
console.log("start");
start = +readline.question()
console.log("finish");
finish = +readline.question()
while (start<finish
){
start = start+1
console.log(start);
}

}
}incrementor();