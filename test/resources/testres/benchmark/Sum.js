function entry() {
    var sum = 0;
    var count = 10000000;
    while(count) {
        sum = sum + 1;
        count = count - 1;
    }

    return sum;
}