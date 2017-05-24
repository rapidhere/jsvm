function entry() {
    var count = 10000;
    var t = 0;
    var o;

    while(count) {
        count = count - 1;
        o = new Runner();

        t = t + o.run(function() {
           return count;
        });
    }

    return t;
}