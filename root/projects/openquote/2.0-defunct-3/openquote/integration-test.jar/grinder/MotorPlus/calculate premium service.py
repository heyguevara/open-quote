# Motor Plus test script
# This script invokes the junit test to calculate premium for a Motor Plus quotation. This only involves
# invoking the service itself and is aimed at getting a picture of the maximum throughput of the service
# devorced from any UI interaction.

from com.ail.openquote.motorplus import MotorPlusPerformanceTest
from net.grinder.script import Test

test=Test(1, "Calculate premium");

# A TestRunner instance is created for each thread. It can be used to
# store thread-specific data.
class TestRunner:

    # This method is called for every run.
    def __call__(self):
        testCase = MotorPlusPerformanceTest()
        j=test.wrap(testCase)
        j.testCalculatePremium()
