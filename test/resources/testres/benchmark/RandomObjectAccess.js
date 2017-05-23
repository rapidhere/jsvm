function entry(obj1, obj2, obj3) {
    var count = 20000;
    var i = 0;
    var obj;
    var t = 0;

    while(count) {
        count = count - 1;
        i = i + 1;
        if (i === 3) {
            i = 0;
        }

        if(i === 0) {
            obj = obj1;
        } else if (i === 1) {
            obj = obj2;
        } else if(i === 2) {
            obj = obj3;
        }

        t = t + obj.field1;
    }

    return t;
}