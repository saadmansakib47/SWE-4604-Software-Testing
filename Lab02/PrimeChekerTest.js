const QUnit = typeof require !== 'undefined' ? require('qunit') : QUnit;
const PrimeChecker = typeof require !== 'undefined' ? require('./PrimeChecker') : PrimeChecker;

QUnit.test("2 should be prime", assert => {
  assert.ok(PrimeChecker.isPrimeNumber(2), "2 is prime");
});

QUnit.test("47 should be prime", assert => {
  assert.ok(PrimeChecker.isPrimeNumber(47), "47 is prime");
});

QUnit.test("20 should not be prime", assert => {
  assert.notOk(PrimeChecker.isPrimeNumber(20), "20 is not prime");
});

QUnit.test("933 should not be prime", assert => {
  assert.notOk(PrimeChecker.isPrimeNumber(933), "933 is not prime");
});

QUnit.test("1000000000000 should not be prime", assert => {
  assert.notOk(PrimeChecker.isPrimeNumber(1000000000000), "1000000000000 is not prime");
});

QUnit.test("Should throw on non-integer input (2.5)", assert => {
  assert.throws(() => PrimeChecker.isPrimeNumber(2.5), "Throws for non-integer input");
});

QUnit.test("Should throw on less than 2 (1)", assert => {
  assert.throws(() => PrimeChecker.isPrimeNumber(1), "Throws for integer less than 2");
});

QUnit.test("Should throw on negative input (-14)", assert => {
  assert.throws(() => PrimeChecker.isPrimeNumber(-14), "Throws for negative input");
});

QUnit.test("Should throw if too large (10000000000000000000)", assert => {
    assert.throws(() => PrimeChecker.isPrimeNumber(10000000000000000000), "Throws for too large input");
})
