#!/bin/bash

clear && geth attach http://127.0.0.1:8545
personal.unlockAccount("ADDR")
eth.getBalance("ADDR")
web3.eth.accounts
eth.default = "ADDR"
miner.start()

exit 0
