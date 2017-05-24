function entry(obj) {
    var count = 500000;
    var i = 0;
    var methodName;
    var t = 0;

    while(count) {
        count = count - 1;
        i = i + 1;
        if (i === 4) {
            i = 0;
        }

        if(i === 0) {
            methodName = 'method1';
        } else if (i === 1) {
            methodName = 'method2';
        } else if(i === 2) {
            methodName = 'method3';
        } else if(i === 3) {
            methodName = 'method4';
        }

        t = t + obj[methodName](i);
    }

    return t;
}