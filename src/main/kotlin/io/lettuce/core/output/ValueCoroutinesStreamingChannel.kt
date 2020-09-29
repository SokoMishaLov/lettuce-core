package io.lettuce.core.output

import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.sendBlocking

/**
 * @author sokomishalov
 */
class ValueCoroutinesStreamingChannel<V : Any>(
        private val channel: SendChannel<V>
) : ValueStreamingChannel<V> {

    override fun onValue(value: V) {
        channel.sendBlocking(value)
    }

    override fun onComplete() {
        channel.close()
    }

    override fun onError(cause: Throwable) {
        channel.close(cause)
    }
}