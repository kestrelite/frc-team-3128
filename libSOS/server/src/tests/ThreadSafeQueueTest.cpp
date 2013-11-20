/*------------------------------------------------------------------------------
	ThreadSafeQueueTest.cpp

	<summary>

	Copyright (c) 2013 Qualcomm, Inc. All rights reserved.  
	Confidential and Proprietary.
	This software may be subject to U.S. and international export laws.
	Diversion contrary to U.S. and international laws is strictly prohibited.
------------------------------------------------------------------------------*/

#include <string>
#include <memory>
#include <boost/test/unit_test.hpp>
#include <ThreadSafeQueue.h>

namespace m2mslap
{


BOOST_AUTO_TEST_SUITE(ThreadSafeQueueTestSuite)


/*-----------------------------------------------------------------------------
    Test Case: Really basic.  Not too much to test, other than the
    synchronization capability, which can't really be unit tested.
 ----------------------------------------------------------------------------*/

BOOST_AUTO_TEST_CASE(Basic)
{
    // Construct empty queue of int's.
    ThreadSafeQueue<int>    queue;
    BOOST_CHECK_EQUAL(queue.size(), ThreadSafeQueue<int>::size_type(0));


    // Insert some items.
    queue.Enqueue(1);           // enqueue at back
    queue.Enqueue(2);           // enqueue at back
    queue.Enqueue(3, false);    // enqueue at back
    queue.Enqueue(4, true);     // enqueue at front
    BOOST_CHECK_EQUAL(queue.size(), ThreadSafeQueue<int>::size_type(4));


    // Pop the items, verify size.
    BOOST_CHECK_EQUAL(queue.Dequeue(), 4);
    BOOST_CHECK_EQUAL(queue.size(), ThreadSafeQueue<int>::size_type(3));

    BOOST_CHECK_EQUAL(queue.Dequeue(), 1);
    BOOST_CHECK_EQUAL(queue.size(), ThreadSafeQueue<int>::size_type(2));

    BOOST_CHECK_EQUAL(queue.Dequeue(), 2);
    BOOST_CHECK_EQUAL(queue.size(), ThreadSafeQueue<int>::size_type(1));

    BOOST_CHECK_EQUAL(queue.Dequeue(), 3);
    BOOST_CHECK_EQUAL(queue.size(), ThreadSafeQueue<int>::size_type(0));
}


BOOST_AUTO_TEST_SUITE_END()

} /* namespace m2mslap */

