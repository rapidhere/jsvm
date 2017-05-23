/* The Great Computer Language Shootout
 http://shootout.alioth.debian.org/
 contributed by Isaac Gouy */

function entry(obj1, obj2, obj3, Math, print) {

    var PI = 3.141592653589793;
    var SOLAR_MASS = 4 * PI * PI;
    var DAYS_PER_YEAR = 365.24;
    var unused;

    function Body(x, y, z, vx, vy, vz, mass) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
        this.mass = mass;
        return 0;
    }

    Body.prototype.offsetMomentum = function (px, py, pz) {
        this.vx = 0 - px / SOLAR_MASS;
        this.vy = 0 - py / SOLAR_MASS;
        this.vz = 0 - pz / SOLAR_MASS;
        return this;
    };

    function Jupiter() {
        return new Body(
            4.841431442464721,
            0-1.1603200440274284,
            0-0.10362204447112311,
            0.001660076642744037 * DAYS_PER_YEAR,
            0.007699011184197404 * DAYS_PER_YEAR,
            0-6.90460016972063E-5 * DAYS_PER_YEAR,
            9.547919384243266E-4 * SOLAR_MASS
        );
    }

    function Saturn() {
        return new Body(
            8.34336671824458,
            4.124798564124305,
            0-0.4035234171143214,
            0-0.002767425107268624 * DAYS_PER_YEAR,
            0.004998528012349172 * DAYS_PER_YEAR,
            2.3041729757376393E-5 * DAYS_PER_YEAR,
            2.858859806661308E-4 * SOLAR_MASS
        );
    }

    function Uranus() {
        return new Body(
            12.894369562139131,
            15.111151401698631,
            0.22330757889265573,
            0.002964601375647616 * DAYS_PER_YEAR,
            0.0023784717395948095 * DAYS_PER_YEAR,
            0-2.9658956854023756E-5 * DAYS_PER_YEAR,
            4.366244043351563E-5 * SOLAR_MASS
        );
    }

    function Neptune() {
        return new Body(
            15.379697114850917,
            25.919314609987964,
            0.17925877295037118,
            0.0026806777249038932 * DAYS_PER_YEAR,
            0.001628241700382423 * DAYS_PER_YEAR,
            0-9.515922545197159E-5 * DAYS_PER_YEAR,
            5.1513890204661145E-5 * SOLAR_MASS
        );
    }

    function Sun() {
        return new Body(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, SOLAR_MASS);
    }


    function NBodySystem(bodies) {
        this.bodies = bodies;
        var px = 0.0;
        var py = 0.0;
        var pz = 0.0;
        var size = this.bodies.length;
        var i = 0;
        while (i < size) {
            var b = this.bodies[i];
            var m = b.mass;
            px = px + b.vx * m;
            py = py + b.vy * m;
            pz = pz + b.vz * m;
            i = i + 1;
        }
        this.bodies[0].offsetMomentum(px, py, pz);
        return 0;
    }

    NBodySystem.prototype.advance = function (dt) {
        var dx, dy, dz, distance, mag;
        var size = this.bodies.length;

        var i = 0, j;
        while (i < size) {
            var bodyi = this.bodies[i];
            j = i + 1;
            while (j < size) {
                var bodyj = this.bodies[j];
                dx = bodyi.x - bodyj.x;
                dy = bodyi.y - bodyj.y;
                dz = bodyi.z - bodyj.z;

                distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
                mag = dt / (distance * distance * distance);

                bodyi.vx = bodyi.vx - dx * bodyj.mass * mag;
                bodyi.vy = bodyi.vy - dy * bodyj.mass * mag;
                bodyi.vz = bodyi.vz - dz * bodyj.mass * mag;

                bodyj.vx = bodyj.vx + dx * bodyi.mass * mag;
                bodyj.vy = bodyj.vy + dy * bodyi.mass * mag;
                bodyj.vz = bodyj.vz + dz * bodyi.mass * mag;
                j = j + 1;
            }
            i = i + 1;
        }

        i = 0;
        while (i < size) {
            var body = this.bodies[i];
            body.x = body.x + dt * body.vx;
            body.y = body.y + dt * body.vy;
            body.z = body.z + dt * body.vz;
            i = i + 1;
        }
        return 0;
    };

    NBodySystem.prototype.energy = function () {
        var dx, dy, dz, distance;
        var e = 0.0;
        var size = this.bodies.length;

        var i = 0;
        while (i < size) {
            var bodyi = this.bodies[i];

            e = e + 0.5 * bodyi.mass *
                ( bodyi.vx * bodyi.vx
                + bodyi.vy * bodyi.vy
                + bodyi.vz * bodyi.vz );

            var j = i + 1;
            while (j < size) {
                var bodyj = this.bodies[j];
                dx = bodyi.x - bodyj.x;
                dy = bodyi.y - bodyj.y;
                dz = bodyi.z - bodyj.z;

                distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
                e = e - (bodyi.mass * bodyj.mass) / distance;
                j = j + 1;
            }
            i = i + 1;
        }
        return e;
    };

    var ret = 0;
    var n = 3;

    while (n < 24) {
        unused = (function () {
            var bodies = new NBodySystem([
                Sun(), Jupiter(), Saturn(), Uranus(), Neptune()
            ]);
            var max = n * 100;

            ret = ret + bodies.energy();
            var i = 0;
            while (i < max) {
                unused = bodies.advance(0.01);
                i = i + 1;
            }
            ret = ret + bodies.energy();
            return 0;
        })();

        n = n * 2;
    }

    var expected = 0 - 1.3524862408537381;
    return ret;
}