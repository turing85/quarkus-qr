#!/usr/bin/env bash
set -e

function relevant_libs() {
  local relevant_libs=( \
    'libbz2' \
    'libfreetype' \
    'libpng16' \
  )
  echo "${relevant_libs[*]}"
}

function relevant_libs_regex() {
  echo ".*($(IFS='|'; echo "$(relevant_libs)"))\.so.*"
}


function copy_lib64_to_pwd() {
  cp \
   --no-dereference \
   --preserve=links \
   --recursive \
   /usr/lib64/* .
 }

function files_in_pwd() {
  local files=();
  mapfile \
    -t `# truncate trailing delimiter` \
    files < <(ls)
  echo "${files[*]}"
}

function libs_in_pwd_to_keep() {
  local files_to_keep=()
  local relevant_libs_regex="$(relevant_libs_regex)"
  for file in "${@}"
  do
    if [[ "${file}" =~ ${relevant_libs_regex} ]]
    then
      files_to_keep+=( "${file}" )
    fi
  done
  echo "${files_to_keep[*]}"
}

function remove_unwanted_libs_from_pwd() {
  local files=()
  mapfile \
    -d ' ' `# set delimiter` \
    -t `# truncate trailing delimiter` \
    files < <(files_in_pwd)
  local libs_to_keep=()
  mapfile \
    -d ' ' `# set delimiter` \
    -t `# truncate trailing delimiter` \
    libs_to_keep < <(libs_in_pwd_to_keep "${files[@]}")
  for file in "${files[@]}"
  do
    file="${file%%[[:space:]]}"
    if [[ ! "${libs_to_keep[*]}" =~ (^|.*[[:space:]]+)"${file}"([[:space:]]+.*|$).* ]]
    then
      rm -rf "${file}"
    fi
  done
}

function run() {
  copy_lib64_to_pwd
  remove_unwanted_libs_from_pwd
}

run