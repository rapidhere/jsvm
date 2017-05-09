function entry(begin, step, count) {
    var sum = begin;
    while(count) {
        sum = sum + step;
        count = count - 1;
    }

    return sum;
}