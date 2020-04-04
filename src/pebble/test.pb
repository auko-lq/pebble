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
