/*
 * The MIT License
 *
 * Copyright 2020 randalkamradt.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package net.kamradtfamily.springcontroller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author randalkamradt
 */
@Slf4j
@Component
public class SpringServiceImpl {
    private final ReactiveMongoTemplate mongo;

    public SpringServiceImpl(final ReactiveMongoTemplate mongo) {
        this.mongo = mongo;
    }
    
    public Mono<Input> save(final Mono<Input> input) throws IncomingException {
        return mongo.save(input
                .map(i -> mapToData(i)))
            .map(m -> mapToInput(m));
    }

    public Flux<Input> alloutput() throws IncomingException {
        return mongo
                .findAll(MongoData.class)
                .map(m -> mapToInput(m));
    }

    public Mono<Input> output(final String key) throws IncomingException {
        return mongo
                .findById(key, MongoData.class)
                .map(m -> mapToInput(m));
    }

    private Input mapToInput(final MongoData m) {
        return Input.builder()
                .key(m.getId())
                .value(m.getName())
                .optionalValue(m.getDescription())
                .build();
    }

    private MongoData mapToData(final Input i) {
        return MongoData.builder()
                .id(i.getKey())
                .name(i.getValue())
                .description(i.getOptionalValue())
                .build();
    }

    
}
