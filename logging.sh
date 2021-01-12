#! /bin/bash

exit_script() {
  if [[ -e "app.pid" ]]; then
    pid=$(cat app.pid)
    kill -15 $pid
  fi
}

trap exit_script SIGINT SIGTERM

cd dev/Java/TerminalEngine/
printf '\e[3;1442;0t'
clear
tail -f -n100 logs.log | awk '
  /INFO/ {print "\033[32m" $0 "\033[39m"; next}
  /SEVERE/ {print "\033[31m" $0 "\033[39m"; next}
  1 {print}
'