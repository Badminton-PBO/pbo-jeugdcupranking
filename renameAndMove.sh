#!/bin/bash

cd /Users/dekeyser/proef/BK_Jeugd_2024/Spelers_All_resized_V1211_2302ls/

for file in *.jpg; do
    new_name=$(echo $file | sed 's/^\(.\{8\}\)_.*.jpg/\1.jpg/')
    mv "$file" /Users/dekeyser/checkouts/pbo-jeugdcupranking/core/pics/players/"$new_name"
done

for file in *.JPG; do
    new_name=$(echo $file | sed 's/^\(.\{8\}\)_.*.JPG/\1.jpg/')
    mv "$file" /Users/dekeyser/checkouts/pbo-jeugdcupranking/core/pics/players/"$new_name"
done
