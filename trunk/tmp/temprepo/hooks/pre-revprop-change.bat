rem Only allow log messages to be changed.
if “%4? == “svn:log” exit 0
echo Property ‘%4' cannot be changed >&2
exit 1