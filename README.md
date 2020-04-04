# Pebble

Reference from [Stone](https://github.com/chibash/stone)

Added comments on it, and added own implementation syntax

Implement this language just to try something new, so I named it Pebble, simple but beautiful

## start

Enter the `pebble/run/interpreter/BasicInterpreter` file

Then modify the `fileName` field to your own test file

Finally, run `pebble/run/Runner`

## example

```
class A {
    var a = [1,2,3];
    def testA(){
        var i = 0;
        while(i < length(a)){
            print(a[i] + "  ")
            i = i + 1
        }
    }
}

class B extends A{
    var b = [4,5,6];
    def testB(){
        var i = 0;
        while(i < length(b)){
            print(b[i] + "  ")
            i = i + 1
        }
    }
}

var a = A.new;
var b = B.new;

println("A : ")
a.testA()
println("B : ")
b.testB()
println("super : ")
b.testA()
```

## Todo

- [x] Lexer
- [x] Parser
- [x] AST
- [x] if statement
- [x] "else if" support
- [x] while statement
- [x] define variables by "var"
- [x] function
- [x] closure
- [x] scope management
- [x] associated Java syntax
- [x] array
- [x] OOP
- [ ] decimal operations
- [ ] optimization in all aspects
- [ ] run from the command line

......