function fib(n) {
    var ret;
    if(n === 0) {
        ret = 0;
    } else if (n === 1) {
        ret = 1;
    } else {
        ret = fib(n - 1) + fib(n - 2);
    }

    return ret;
}

function entry(n) {
    return fib(n);
}