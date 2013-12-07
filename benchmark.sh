#!/bin/sh

echo ShantenAnalysis
time java -cp shanten-benchmark-assembly-0.1.0-SNAPSHOT.jar com.github.kmizu.shanten_benchmark.ShantenAnalysis

echo ShantenAnalysisOptimized
time java -cp shanten-benchmark-assembly-0.1.0-SNAPSHOT.jar com.github.kmizu.shanten_benchmark.ShantenAnalysisOptimized

echo ShantenAnalysisUsingWhile
time java -cp shanten-benchmark-assembly-0.1.0-SNAPSHOT.jar com.github.kmizu.shanten_benchmark.ShantenAnalysisUsingWhile

echo ShantenAnalysisUsingWhileOptimized
time java -cp shanten-benchmark-assembly-0.1.0-SNAPSHOT.jar com.github.kmizu.shanten_benchmark.ShantenAnalysisUsingWhileOptimized
