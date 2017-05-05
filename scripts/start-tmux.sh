#!/bin/zsh

source "${0:a:h}/.common.sh"

function initiate-tmux() {
  # Initiate with TmuxSessionName
  new-session;

  MainW=$TmuxSessionName:0
  set-option $MainW allow-rename off > /dev/null;
  splitwh-setup $MainW '.' \
      'sbt' \
      'git status' \
  ;
  select-pane $MainW -L; send-to $MainW Enter;
  select-pane $MainW -R; send-to $MainW Enter;
  rename-window $MainW 'root';

  CoreW=$TmuxSessionName:1
  new-window-splitwh-setup $CoreW 'modules/core' 'Core';
  select-pane $CoreW -L;
  send-to $CoreW \
      '../..' Enter \
      'sbt' Enter \
      'project core' Enter \
  ;
  select-pane $CoreW -R;

  select-window $MainW;
}

if ! is-initiated; then
  initiate-tmux
fi

attach-tmux
