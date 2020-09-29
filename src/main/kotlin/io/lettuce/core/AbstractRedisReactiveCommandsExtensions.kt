package io.lettuce.core

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import reactor.util.function.Tuple2

/**
 * @author sokomishalov
 */

internal fun <K : Any, V : Any> AbstractRedisReactiveCommands<K, V>.hgetallFlow(key: K): Flow<Pair<K, V>> {
    return createDissolvingFlux<List<Tuple2<K, V>>, Tuple2<K, V>> { commandBuilder.hgetallFlux(key) }
            .map { it.t1 to it.t2 }
            .asFlow()
}