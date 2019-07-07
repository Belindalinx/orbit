const TestToken = artifacts.require("./OrbitEcoToken.sol");
module.exports = function(deployer) {
    deployer.deploy(TestToken);
};
