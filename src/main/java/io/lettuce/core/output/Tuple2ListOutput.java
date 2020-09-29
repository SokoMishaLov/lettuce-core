package io.lettuce.core.output;

import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.internal.LettuceAssert;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;

/**
 * {@link List} of tuples.
 *
 * @param <K> Key type.
 * @param <V> Value type.
 * @author Mark Paluch
 */
public class Tuple2ListOutput<K, V> extends CommandOutput<K, V, List<Tuple2<K, V>>> implements StreamingOutput<Tuple2<K, V>> {

    private boolean initialized;

    private Subscriber<Tuple2<K, V>> subscriber;

    private K key;

    public Tuple2ListOutput(RedisCodec<K, V> codec) {
        super(codec, Collections.emptyList());
        setSubscriber(ListSubscriber.instance());
    }

    @Override
    public void set(ByteBuffer bytes) {
        if (key == null) {
            key = (bytes == null) ? null : codec.decodeKey(bytes);
            return;
        }
        if (bytes == null) {
            return;
        }

        V value = codec.decodeValue(bytes);
        Tuple2<K, V> tuple = Tuples.of(key, value);
        output.add(tuple);
        key = null;
        subscriber.onNext(output, tuple);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void set(long integer) {
        if (key == null) {
            key = (K) Long.valueOf(integer);
            return;
        }

        V value = (V) Long.valueOf(integer);
        Tuple2<K, V> tuple = Tuples.of(key, value);
        output.add(tuple);
        key = null;
        subscriber.onNext(output, tuple);
    }

    @Override
    public void multi(int count) {
        if (!initialized) {
            output = OutputFactory.newList(count);
            initialized = true;
        }
    }

    @Override
    public void setSubscriber(Subscriber<Tuple2<K, V>> subscriber) {
        LettuceAssert.notNull(subscriber, "Subscriber must not be null");
        this.subscriber = subscriber;
    }

    @Override
    public Subscriber<Tuple2<K, V>> getSubscriber() {
        return subscriber;
    }
}