#!/bin/bash
clear

echo ""

echo "BBBBBBBBBBBBBBBBB   lllllll                                      kkkkkkkk                           hhhhhhh                                 iiii                   "
echo "B::::::::::::::::B  l:::::l                                      k::::::k                           h:::::h                                i::::i                  "
echo "B::::::BBBBBB:::::B l:::::l                                      k::::::k                           h:::::h                                 iiii                   "
echo "BB:::::B     B:::::Bl:::::l                                      k::::::k                           h:::::h                                                        "
echo "  B::::B     B:::::B l::::l    ooooooooooo       cccccccccccccccc k:::::k    kkkkkkk cccccccccccccccch::::h hhhhh         aaaaaaaaaaaaa   iiiiiiinnnn  nnnnnnnn    "
echo "  B::::B     B:::::B l::::l  oo:::::::::::oo   cc:::::::::::::::c k:::::k   k:::::kcc:::::::::::::::ch::::hh:::::hhh      a::::::::::::a  i:::::in:::nn::::::::nn  "
echo "  B::::BBBBBB:::::B  l::::l o:::::::::::::::o c:::::::::::::::::c k:::::k  k:::::kc:::::::::::::::::ch::::::::::::::hh    aaaaaaaaa:::::a  i::::in::::::::::::::nn "
echo "  B:::::::::::::BB   l::::l o:::::ooooo:::::oc:::::::cccccc:::::c k:::::k k:::::kc:::::::cccccc:::::ch:::::::hhh::::::h            a::::a  i::::inn:::::::::::::::n"
echo "  B::::BBBBBB:::::B  l::::l o::::o     o::::oc::::::c     ccccccc k::::::k:::::k c::::::c     ccccccch::::::h   h::::::h    aaaaaaa:::::a  i::::i  n:::::nnnn:::::n"
echo "  B::::B     B:::::B l::::l o::::o     o::::oc:::::c              k:::::::::::k  c:::::c             h:::::h     h:::::h  aa::::::::::::a  i::::i  n::::n    n::::n"
echo "  B::::B     B:::::B l::::l o::::o     o::::oc:::::c              k:::::::::::k  c:::::c             h:::::h     h:::::h a::::aaaa::::::a  i::::i  n::::n    n::::n"
echo "  B::::B     B:::::B l::::l o::::o     o::::oc::::::c     ccccccc k::::::k:::::k c::::::c     ccccccch:::::h     h:::::ha::::a    a:::::a  i::::i  n::::n    n::::n"
echo "BB:::::BBBBBB::::::Bl::::::lo:::::ooooo:::::oc:::::::cccccc:::::ck::::::k k:::::kc:::::::cccccc:::::ch:::::h     h:::::ha::::a    a:::::a i::::::i n::::n    n::::n"
echo "B:::::::::::::::::B l::::::lo:::::::::::::::o c:::::::::::::::::ck::::::k  k:::::kc:::::::::::::::::ch:::::h     h:::::ha:::::aaaa::::::a i::::::i n::::n    n::::n"
echo "B::::::::::::::::B  l::::::l oo:::::::::::oo   cc:::::::::::::::ck::::::k   k:::::kcc:::::::::::::::ch:::::h     h:::::h a::::::::::aa:::ai::::::i n::::n    n::::n"
echo "BBBBBBBBBBBBBBBBB   llllllll   ooooooooooo       cccccccccccccccckkkkkkkk    kkkkkkk cccccccccccccccchhhhhhh     hhhhhhh  aaaaaaaaaa  aaaaiiiiiiii nnnnnn    nnnnnn"
                                                                                                                                                                   
echo ""                                                                                                                                                            

geth --datadir "./db" --networkid 5777 --rpc --rpcport "8545" --rpccorsdomain "*" --nodiscover --rpcapi="admin,db,eth,debug,miner,net,shh,txpool,personal,web3"
