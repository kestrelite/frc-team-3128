/*
 * LogHolder.h
 *
 *  Created on: Jan 1, 2014
 *      Author: jamie
 */

#ifndef LOGHOLDER_H_
#define LOGHOLDER_H_

#include <LogMessage.h>
#include <LogCore.h>
#include <memory>
#include <Tags.h>

#define LOG_DEBUG(location, args)																				\
{																									\
	std::shared_ptr<LogMessage> logMessage(new LogMessage({{"time", currentTime()}, {"severity", "Debug"}, {"location", location}}));	\
	logMessage->stream() << args;\
	LogCore::instance().log(logMessage); \
}

#define LOG_INFO(location, args)																				\
{																									\
	std::shared_ptr<LogMessage> logMessage(new LogMessage({{"time", currentTime()}, {"severity", "Info"}, {"location", location}}));	\
	logMessage->stream() << args;\
	LogCore::instance().log(logMessage); \
}

#define LOG_UNUSUAL(location, args)																				\
{																									\
	std::shared_ptr<LogMessage> logMessage(new LogMessage({{"time", currentTime()}, {"severity", "Unusual"}, {"location", location}}));	\
	logMessage->stream() << args;\
	LogCore::instance().log(logMessage); \
}


#define LOG_RECOVERABLE(location, args)																				\
{																									\
	std::shared_ptr<LogMessage> logMessage(new LogMessage({{"time", currentTime()}, {"severity", "Recoverable"}, {"location", location}}));	\
	logMessage->stream() << args;\
	LogCore::instance().log(logMessage); \
}


#define LOG_FATAL(location, args)																				\
{																									\
	std::shared_ptr<LogMessage> logMessage(new LogMessage({{"time", currentTime()}, {"severity", "Fatal"}, {"location", location}}));	\
	logMessage->stream() << args;\
	LogCore::instance().log(logMessage); \
}

#endif /* LOGHOLDER_H_ */
