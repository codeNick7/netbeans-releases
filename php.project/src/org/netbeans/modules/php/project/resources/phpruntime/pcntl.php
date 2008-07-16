<?php

// Start of pcntl v.

/**
 * Forks the currently running process
 * @link http://php.net/manual/en/function.pcntl-fork.php
 * @return int On success, the PID of the child process is returned in the
 * parent's thread of execution, and a 0 is returned in the child's
 * thread of execution. On failure, a -1 will be returned in the
 * parent's context, no child process will be created, and a PHP
 * error is raised.
 * </p>
 */
function pcntl_fork () {}

/**
 * Waits on or returns the status of a forked child
 * @link http://php.net/manual/en/function.pcntl-waitpid.php
 * @param pid int <p>
 * The value of pid can be one of the following:
 * <table>
 * possible values for pid
 * <tr valign="top">
 * <td>&lt; -1</td>
 * <td>
 * wait for any child process whose process group ID is equal to
 * the absolute value of pid.
 * </td>
 * </tr>
 * <tr valign="top">
 * <td>-1</td>
 * <td>
 * wait for any child process; this is the same behaviour that
 * the wait function exhibits.
 * </td>
 * </tr>
 * <tr valign="top">
 * <td>0</td>
 * <td>
 * wait for any child process whose process group ID is equal to
 * that of the calling process.
 * </td>
 * </tr>
 * <tr valign="top">
 * <td>&gt; 0</td>
 * <td>
 * wait for the child whose process ID is equal to the value of
 * pid.
 * </td>
 * </tr>
 * </table>
 * </p>
 * <p>
 * Specifying -1 as the pid is
 * equivalent to the functionality pcntl_wait provides
 * (minus options).
 * </p>
 * @param status int <p>
 * pcntl_waitpid will store status information
 * in the status parameter which can be
 * evaluated using the following functions:
 * pcntl_wifexited,
 * pcntl_wifstopped,
 * pcntl_wifsignaled,
 * pcntl_wexitstatus,
 * pcntl_wtermsig and
 * pcntl_wstopsig.
 * </p>
 * @param options int[optional] <p>
 * The value of options is the value of zero
 * or more of the following two global constants
 * OR'ed together:
 * <table>
 * possible values for options
 * <tr valign="top">
 * <td>WNOHANG</td>
 * <td>
 * return immediately if no child has exited.
 * </td>
 * </tr>
 * <tr valign="top">
 * <td>WUNTRACED</td>
 * <td>
 * return for children which are stopped, and whose status has
 * not been reported.
 * </td>
 * </tr>
 * </table>
 * </p>
 * @return int pcntl_waitpid returns the process ID of the
 * child which exited, -1 on error or zero if WNOHANG was used and no
 * child was available
 * </p>
 */
function pcntl_waitpid ($pid, &$status, $options = null) {}

/**
 * Waits on or returns the status of a forked child
 * @link http://php.net/manual/en/function.pcntl-wait.php
 * @param status int <p>
 * pcntl_wait will store status information
 * in the status parameter which can be
 * evaluated using the following functions:
 * pcntl_wifexited,
 * pcntl_wifstopped,
 * pcntl_wifsignaled,
 * pcntl_wexitstatus,
 * pcntl_wtermsig and
 * pcntl_wstopsig.
 * </p>
 * @param options int[optional] <p>
 * If wait3 is available on your system (mostly BSD-style systems), you can
 * provide the optional options parameter. If this
 * parameter is not provided, wait will be used for the system call. If
 * wait3 is not available, providing a value for options
 * will have no effect. The value of options
 * is the value of zero or more of the following two constants
 * OR'ed together:
 * <table>
 * Possible values for options
 * <tr valign="top">
 * <td>WNOHANG</td>
 * <td>
 * Return immediately if no child has exited.
 * </td>
 * </tr>
 * <tr valign="top">
 * <td>WUNTRACED</td>
 * <td>
 * Return for children which are stopped, and whose status has
 * not been reported.
 * </td>
 * </tr>
 * </table>
 * </p>
 * @return int pcntl_wait returns the process ID of the
 * child which exited, -1 on error or zero if WNOHANG was provided as an
 * option (on wait3-available systems) and no child was available.
 * </p>
 */
function pcntl_wait (&$status, $options = null) {}

/**
 * Installs a signal handler
 * @link http://php.net/manual/en/function.pcntl-signal.php
 * @param signo int <p>
 * The signal number.
 * </p>
 * @param handler callback <p>
 * The signal handler which may be the name of a user created function,
 * or method, or either of the two global constants 
 * SIG_IGN or SIG_DFL. 
 * </p>
 * <p>
 * Note that when you set a handler to an object method, that object's
 * reference count is increased which makes it persist until you either
 * change the handler to something else, or your script ends.
 * </p>
 * @param restart_syscalls bool[optional] <p>
 * Specifies whether system call restarting should be used when this
 * signal arrives and defaults to true.
 * </p>
 * @return bool &return.success;
 * </p>
 */
function pcntl_signal ($signo, $handler, $restart_syscalls = null) {}

/**
 * Checks if status code represents a normal exit
 * @link http://php.net/manual/en/function.pcntl-wifexited.php
 * @param status int &pcntl.parameter.status;
 * @return bool true if the child status code represents a normal exit, false
 * otherwise.
 * </p>
 */
function pcntl_wifexited ($status) {}

/**
 * Checks whether the child process is currently stopped
 * @link http://php.net/manual/en/function.pcntl-wifstopped.php
 * @param status int &pcntl.parameter.status;
 * @return bool true if the child process which caused the return is
 * currently stopped, false otherwise.
 * </p>
 */
function pcntl_wifstopped ($status) {}

/**
 * Checks whether the status code represents a termination due to a signal
 * @link http://php.net/manual/en/function.pcntl-wifsignaled.php
 * @param status int &pcntl.parameter.status;
 * @return bool true if the child process exited because of a signal which was
 * not caught, false otherwise.
 * </p>
 */
function pcntl_wifsignaled ($status) {}

/**
 * Returns the return code of a terminated child
 * @link http://php.net/manual/en/function.pcntl-wexitstatus.php
 * @param status int &pcntl.parameter.status;
 * @return int the return code, as an integer.
 * </p>
 */
function pcntl_wexitstatus ($status) {}

/**
 * Returns the signal which caused the child to terminate
 * @link http://php.net/manual/en/function.pcntl-wtermsig.php
 * @param status int &pcntl.parameter.status;
 * @return int the signal number, as an integer.
 * </p>
 */
function pcntl_wtermsig ($status) {}

/**
 * Returns the signal which caused the child to stop
 * @link http://php.net/manual/en/function.pcntl-wstopsig.php
 * @param status int &pcntl.parameter.status;
 * @return int the signal number.
 * </p>
 */
function pcntl_wstopsig ($status) {}

/**
 * Executes specified program in current process space
 * @link http://php.net/manual/en/function.pcntl-exec.php
 * @param path string <p>
 * path must be the path to a binary executable or a
 * script with a valid path pointing to an executable in the shebang (
 * #!/usr/local/bin/perl for example) as the first line. See your system's
 * man execve(2) page for additional information.
 * </p>
 * @param args array[optional] <p>
 * args is an array of argument strings passed to the
 * program.
 * </p>
 * @param envs array[optional] <p>
 * envs is an array of strings which are passed as
 * environment to the program. The array is in the format of name => value,
 * the key being the name of the environmental variable and the value being
 * the value of that variable.
 * </p>
 * @return void false on error and does not return on success.
 * </p>
 */
function pcntl_exec ($path, array $args = null, array $envs = null) {}

/**
 * Set an alarm clock for delivery of a signal
 * @link http://php.net/manual/en/function.pcntl-alarm.php
 * @param seconds int <p>
 * The number of seconds to wait. If seconds is
 * zero, no new alarm is created.
 * </p>
 * @return int the time in seconds that any previously scheduled alarm had
 * remaining before it was to be delivered, or 0 if there
 * was no previously scheduled alarm.
 * </p>
 */
function pcntl_alarm ($seconds) {}

/**
 * Get the priority of any process
 * @link http://php.net/manual/en/function.pcntl-getpriority.php
 * @param pid int[optional] <p>
 * If not specified, the pid of the current process is used.
 * </p>
 * @param process_identifier int[optional] <p>
 * One of PRIO_PGRP, PRIO_USER
 * or PRIO_PROCESS.
 * </p>
 * @return int pcntl_getpriority returns the priority of the process
 * or false on error. A lower numerical value causes more favorable
 * scheduling.
 * </p>
 * &return.falseproblem;
 */
function pcntl_getpriority ($pid = null, $process_identifier = null) {}

/**
 * Change the priority of any process
 * @link http://php.net/manual/en/function.pcntl-setpriority.php
 * @param priority int <p>
 * priority is generally a value in the range
 * -20 to 20. The default priority
 * is 0 while a lower numerical value causes more
 * favorable scheduling. Because priority levels can differ between
 * system types and kernel versions, please see your system's setpriority(2)
 * man page for specific details.
 * </p>
 * @param pid int[optional] <p>
 * If not specified, the pid of the current process is used.
 * </p>
 * @param process_identifier int[optional] <p>
 * One of PRIO_PGRP, PRIO_USER
 * or PRIO_PROCESS.
 * </p>
 * @return bool &return.success;
 * </p>
 */
function pcntl_setpriority ($priority, $pid = null, $process_identifier = null) {}

define ('WNOHANG', 1);
define ('WUNTRACED', 2);
define ('SIG_IGN', 1);
define ('SIG_DFL', 0);
define ('SIG_ERR', -1);
define ('SIGHUP', 1);
define ('SIGINT', 2);
define ('SIGQUIT', 3);
define ('SIGILL', 4);
define ('SIGTRAP', 5);
define ('SIGABRT', 6);
define ('SIGIOT', 6);
define ('SIGBUS', 7);
define ('SIGFPE', 8);
define ('SIGKILL', 9);
define ('SIGUSR1', 10);
define ('SIGSEGV', 11);
define ('SIGUSR2', 12);
define ('SIGPIPE', 13);
define ('SIGALRM', 14);
define ('SIGTERM', 15);
define ('SIGSTKFLT', 16);
define ('SIGCLD', 17);
define ('SIGCHLD', 17);
define ('SIGCONT', 18);
define ('SIGSTOP', 19);
define ('SIGTSTP', 20);
define ('SIGTTIN', 21);
define ('SIGTTOU', 22);
define ('SIGURG', 23);
define ('SIGXCPU', 24);
define ('SIGXFSZ', 25);
define ('SIGVTALRM', 26);
define ('SIGPROF', 27);
define ('SIGWINCH', 28);
define ('SIGPOLL', 29);
define ('SIGIO', 29);
define ('SIGPWR', 30);
define ('SIGSYS', 31);
define ('SIGBABY', 31);
define ('PRIO_PGRP', 1);
define ('PRIO_USER', 2);
define ('PRIO_PROCESS', 0);

// End of pcntl v.
?>
