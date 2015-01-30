if (x > 0) 
{
    alert("> 0");
} else if (x < 0) {
    alert("< 0");
}
else
    {
        alert("zero");
    }

function process(x, y, repeat) {
    while (repeat) {
        do {
            try
            {
                doSomething(x, y);
            } catch (e) {
                repeat = false;
                handleException(e);
            }
                 finally {
                    cleanup();
                 }
        } while (x > y)
    }
}

function doSomething(x, y)
            {
                for (var i = 0, max = x; i < max; i++) {
                    console.log(y);
                }

                switch (y) {
                    case 0:
                        console.log("y = 0");
                        break;
                    case 1:
                        console.log("y = 1");
                        break;
                    default:
                        console.log("y = " + y);
                        break;
                }
            }

function CustomColor(red, green, blue)
    {
    this.red = red;
    this.green = green;
    this.blue = blue;
    }

function createCustomColor() {
var redColor = new CustomColor(255, 0, 0);
with (redColor) {
    if (red > green) {
        console.log("red");
    } else if (green > blue) {
        console.log("green");
    } else {
        console.log("unknown");
    }
}
}