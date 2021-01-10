#! /bin/bash

# credits : https://github.com/fearside/ProgressBar/ - modified
function update_progress_bar() {
    # compute the parameters
    let _size=$1
    let _expected_size=$2
    let _progress=(${_size} * 100)/${_expected_size}
    let _done=${_progress}*2
    let _left=(200-${_done})/4
    
    _empty=$(printf "%${_left}s")
    
    # printf progress bar
    
    bar=""
    
    let to_fill=_done
    for ((i = 0 ; i < to_fill ; i++)); do
        if [[ $(( i % 4 )) == 0 ]]; then
            bar+="▎"
            elif [[ $(( i % 4 )) == 1 ]]; then
            bar="${bar::-1}▌"
            elif [[ $(( i % 4 )) == 2 ]]; then
            bar="${bar::-1}▊"
            elif [[ $(( i % 4 )) == 3 ]]; then
            bar="${bar::-1}█"
        fi
    done
    
    
    echo -ne "                                                             \r"
    printf "│${bar}${_empty// /░} ${_progress}%% │ ${_size}/${_expected_size}Kb         \r"
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

# -------------------------------------------- MAIN --------------------------------------------

cd dev/Java/Terminal_AlgorithmVisualiser/
debug_mode=false
# check if is debug mode
if [ "${1}" = "-d" ]; then
    debug_mode=true
  osascript &>/dev/null <<EOF
    tell application "iTerm"
      create window with default profile
      tell current session of window 1
        write text "bash dev/Java/Terminal_AlgorithmVisualiser/logging.sh"
        set columns to 65
        set rows to 39
      end tell
    end tell
    tell application "System Events" to keystroke "^" using {command down, shift down}
EOF
fi

# delete bin dir
if [ -d "./bin" ]; then
    rm -r ./bin
fi
# create bin dir
mkdir bin
# printf '\u001b[?25l'

# Compile all java files
readonly EXPECTED_BIN_SIZE=384
echo -ne "Compiling files to /bin...\n"
javac -encoding utf8 $(find . -name "*.java") -d bin &
BACK_PID=$!
# wait process to finish
wait_for_process $BACK_PID $EXPECTED_BIN_SIZE "./bin"

SIZE=$(du -d 0 ./bin | cut -f1)
if [ $SIZE -ge $EXPECTED_BIN_SIZE -a -d "./bin" -a $progress -ne 0 ]; then
    echo -ne "\rDone!                                                                         \n\r"
    
    if [ -d "./res" ]; then
        echo -ne "Copying ressources to /bin...                   \n"
        # Copy res/ folder contents to bin/
        cp -a res/. bin/ &
        BACK_PID=$!
        # wait process to finish
        readonly EXPECTED_RES_SIZE=384
        wait_for_process $BACK_PID $EXPECTED_RES_SIZE "./res"
        echo -ne "\rDone!                                                                         \n"
    fi
    
    # Launch main Class (Loop.class)
    java -cp bin com.leo.application.Loop
    
    # End script
    if [ "$debug_mode" = "true" ]; then
    osascript &>/dev/null <<EOF
      tell application "iTerm"
        activate
        delay 0.05
        tell window 2 to select
      end tell
      delay 0.1

      tell application "System Events" to keystroke "c" using {control down}
      delay 0.1

      tell application "iTerm" to close window 1
EOF
    fi
else
    # Red High intensity color
    echo -ne "\n\n\033[0;91m"
    echo -ne "Error: could not compile... Check permissions or if java is installed.\n"
    # Reset color
    echo -en "\033[0m"
fi