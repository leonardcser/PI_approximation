#! /bin/bash

# credits : https://github.com/fearside/ProgressBar/ - modified
function update_progress_bar() {
  # compute the parameters
  let _size=$1
  let _expected_size=$2
  let _progress=(${_size} * 100)/${_expected_size}
  let _done=${_progress}/2
  let _left=50-$_done

  _fill=$(printf "%${_done}s")
  _empty=$(printf "%${_left}s")

  # printf progress bar
  echo -ne "                                                             \r"
  printf "│${_fill// /█}${_empty// /░} ${_progress}%% │ ${_size}/${_expected_size}Kb         \r"
}

declare -i progress=0

function wait_for_process() {
  local _pid=$1
  progress=0
  update_progress_bar progress $2

  while kill -0 $_pid >/dev/null 2>&1; do
    if [ -d $3 ]; then
      SIZE=$(du -d 0 $3 | cut -f1)
      progress=SIZE
    fi
    update_progress_bar progress $2
  done

  if [ $progress -ne 0 ]; then
    update_progress_bar $2 $2
  fi
}

cd dev/Java/Terminal_AlgorithmVisualiser/
if [ -d "./bin" ]; then
  rm -r ./bin
fi
mkdir bin
# Compile all java files
readonly EXPECTED_BIN_SIZE=272
echo -ne "Compiling files to /bin...\n"
javac -encoding utf8 $(find . -name "*.java") -d bin &
BACK_PID=$!
# wait process to finish
wait_for_process $BACK_PID $EXPECTED_BIN_SIZE "./bin"

SIZE=$(du -d 0 ./bin | cut -f1)
if [ $SIZE -ge $EXPECTED_BIN_SIZE -a -d "./bin" -a $progress -ne 0 ]; then
  echo -ne "\rDone!                                                                         \n\r"
  echo -ne "Copying ressources to /bin...                   \n"
  # Copy res/ folder contents to bin/
  cp -a res/. bin/ &
  BACK_PID=$!
  # wait process to finish
  readonly EXPECTED_RES_SIZE=352
  wait_for_process $BACK_PID $EXPECTED_RES_SIZE "./res"
  echo -ne "\rDone!                                                                         \n"

  stty raw -echo

  # Launch main Class (Loop.class)
  java -cp bin com.leo.application.Loop

  # End script
  stty raw echo
  clear
else
  # Red High intensity color
  echo -ne "\n\n\033[0;91m"
  echo -ne "Error: could not compile... Check permissions or if java is installed.\n"
  # Reset color
  echo -en "\033[0m"
fi
