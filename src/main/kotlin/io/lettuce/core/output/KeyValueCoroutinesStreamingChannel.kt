package io.lettuce.core.output

import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.sendBlocking

/**
 * @author sokomishalov
 */
class KeyValueCoroutinesStreamingChannel<K : Any, V : Any>(
        private val channel: SendChannel<Pair<K, V>>
) : KeyValueStreamingChannel<K, V> {

    override fun onKeyValue(key: K, value: V) {
        channel.sendBlocking(key to value)
    }

    override fun onComplete() {
        channel.close()
    }

    override fun onError(cause: Throwable) {
        channel.close(cause)
    }
}