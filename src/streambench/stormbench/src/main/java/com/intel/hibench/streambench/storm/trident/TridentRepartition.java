/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.intel.hibench.streambench.storm.trident;

import com.intel.hibench.streambench.storm.spout.KafkaSpoutFactory;
import com.intel.hibench.streambench.storm.topologies.SingleTridentSpoutTops;
import com.intel.hibench.streambench.storm.trident.functions.Identity;
import com.intel.hibench.streambench.storm.util.StormBenchConfig;
import org.apache.storm.kafka.trident.OpaqueTridentKafkaSpout;
import org.apache.storm.trident.TridentTopology;
import org.apache.storm.tuple.Fields;

public class TridentRepartition extends SingleTridentSpoutTops {

  public TridentRepartition(StormBenchConfig config) {
    super(config);
  }

  @Override
  public TridentTopology createTopology() {
    OpaqueTridentKafkaSpout spout = KafkaSpoutFactory.getTridentSpout(config);

    TridentTopology topology = new TridentTopology();

    topology.newStream("bg0", spout)
        .parallelismHint(config.spoutThreads)
        .shuffle()
        .each(spout.getOutputFields(), new Identity(config),
            new Fields("tuple"))
        .parallelismHint(config.boltThreads);
    return topology;
  }
}