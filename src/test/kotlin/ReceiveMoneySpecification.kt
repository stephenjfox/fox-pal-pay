import io.kotlintest.specs.DescribeSpec

/**
 * Created by Stephen Fox on 3/21/19.
 */
class ReceiveMoneySpecification : DescribeSpec({

    describe("Accepted cases") {
        context("Simple") {
            it("The receiver accepts the request, the sender has the funds for the transaction")
        }
        it("The receiver has a request out to the sender") {}
        it("The receiver has no pending requests with the sender") {}
        // TODO when a user
    }

    describe("Rejected cases") {
        context("Simple") {
            it("The receiver rejects, the sender has funds for the transaction")
        }
        it("The receiver rejects") {}
        it("The receiver has no pending requests with the sender") {}
        // TODO
    }

    describe("Failure cases") {
        // TODO: test when subsystems "go down"
    }

})