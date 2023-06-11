#!/usr/bin/env bash
set -e

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

function files_in_pwd_to_keep() {
  local files_to_keep=()
  for file in "${@}"
  do
    if [[ "${file}" =~ .*(libfreetype|libbz2|libpng16)\.so.* ]]
    then
      files_to_keep+=( "${file}" )
    fi
  done
  echo "${files_to_keep[*]}"
}

function remove_unwanted_files_from_pwd() {
  local files=()
  mapfile \
    -d ' ' `# set delimiter` \
    -t `# truncate trailing delimiter` \
    files < <(files_in_pwd)
  local files_to_keep=()
  mapfile \
    -d ' ' `# set delimiter` \
    -t `# truncate trailing delimiter` \
    files_to_keep < <(files_in_pwd_to_keep "${files[@]}")
  for file in "${files[@]}"
  do
    file="${file%%[[:space:]]}"
    if [[ ! "${files_to_keep[*]}" =~ (^|.*[[:space:]]+)"${file}"([[:space:]]+.*|$).* ]]
    then
      rm -rf "${file}"
    fi
  done
}

function run() {
  copy_lib64_to_pwd
  remove_unwanted_files_from_pwd
}

run