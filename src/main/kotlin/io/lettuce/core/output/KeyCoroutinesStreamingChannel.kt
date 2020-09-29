package io.lettuce.core.output

import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.sendBlocking

/**
 * @author sokomishalov
 */
class KeyCoroutinesStreamingChannel<K : Any>(
        private val channel: SendChannel<K>
) : KeyStreamingChannel<K> {

    override fun onKey(key: K) {
        channel.sendBlocking(key)
    }

    override fun onComplete() {
        channel.close()
    }

    override fun onError(cause: Throwable) {
        channel.close(cause)
    }
}