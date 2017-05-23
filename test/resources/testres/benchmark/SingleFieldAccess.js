function entry(obj) {
    var count = 1000000;
    var t = 0;

    while(count) {
        count = count - 1;

        t = t + obj.field1;
    }

    return t;
}