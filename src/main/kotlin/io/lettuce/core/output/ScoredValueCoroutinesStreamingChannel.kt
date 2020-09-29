package io.lettuce.core.output

import io.lettuce.core.ScoredValue
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.sendBlocking

/**
 * @author sokomishalov
 */
class ScoredValueCoroutinesStreamingChannel<V : Any>(
        private val channel: SendChannel<ScoredValue<V>>
) : ScoredValueStreamingChannel<V> {

    override fun onValue(value: ScoredValue<V>) {
        channel.sendBlocking(value)
    }

    override fun onComplete() {
        channel.close()
    }

    override fun onError(cause: Throwable) {
        channel.close(cause)
    }
}