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

  LinquisitorJVMW=$TmuxSessionName:1
  new-window-splitwh-setup $LinquisitorJVMW 'modules/core' 'LinquisitorJVM';
  select-pane $LinquisitorJVMW -L;
  send-to $LinquisitorJVMW \
      '../..' Enter \
      'sbt' Enter \
      'project linquisitorJVM' Enter \
  ;
  select-pane $LinquisitorJVMW -R;

  LinquisitorJSW=$TmuxSessionName:2
  new-window-splitwh-setup $LinquisitorJSW 'modules/core' 'LinquisitorJS';
  select-pane $LinquisitorJSW -L;
  send-to $LinquisitorJSW \
      '../..' Enter \
      'sbt' Enter \
      'project linquisitorJS' Enter \
  ;
  select-pane $LinquisitorJSW -R;

  select-window $MainW;
}

if ! is-initiated; then
  initiate-tmux
fi

attach-tmux
