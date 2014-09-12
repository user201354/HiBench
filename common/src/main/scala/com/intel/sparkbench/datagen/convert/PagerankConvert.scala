// Copyright (C) 2011-2012 the original author or authors.
// See the LICENCE.txt file distributed with this work for additional
// information regarding copyright ownership.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.intel.sparkbench.datagen.convert

import org.apache.hadoop.conf.Configuration
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Created by lv on 14-9-12.
 */
object PagerankConvert{
    val conf = new Configuration()
    def main(args: Array[String]){
      if (args.length != 3){
        System.err.println("Usage: Convert <input_directory> <output_file_path> <PARALLEL>")
        System.exit(1)
      }

      val input_path = args(0)  //"/HiBench/Pagerank/Input/edges"
      val output_name = args(1) // "/HiBench/Pagerank/Input/edges.txt"
      val parallel = args(2).toInt  //256

      val sparkConf = new SparkConf().setAppName("HiBench PageRank Converter")
      val sc = new SparkContext(sparkConf)

      val data = sc.textFile(input_path).map{case(line)=>
          val elements = line.split('\t')
          "%s  %s".format(elements(1), elements(2))
      }

      data.repartition(parallel)
      data.saveAsTextFile(output_name)
      sc.stop()
    }
  }