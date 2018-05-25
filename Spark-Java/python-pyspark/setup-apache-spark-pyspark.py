import os
import sys
import random

# winutils.exe chmod -R 777 C:\tmp\hive
# winutils.exe ls -F C:\tmp\hive

# Path for spark source folder
os.environ['SPARK_HOME'] = "C:\Spark\spark-2.3.0-bin-hadoop2.7"

# Append pyspark to Python Path
#sys.path.append("C:\Spark\spark-2.3.0-bin-hadoop2.7\python")
#sys.path.append("C:\Spark\spark-2.3.0-bin-hadoop2.7\python\lib\py4j-0.10.6-src.zip")

try:
    from pyspark import SparkContext
    from pyspark import SparkConf
    from pyspark.sql import SparkSession
    import pyspark
    print("Successfully imported Spark Modules")

except ImportError as e:
    print("Can not import Spark Modules", e)
    sys.exit(1)


# import findspark
# findspark.init()
sc0 = pyspark.SparkContext(appName="Pi")
num_samples = 100000000


def inside(p):
    x, y = random.random(), random.random()
    return x*x + y*y < 1


count = sc0.parallelize(range(0, num_samples)).filter(inside).count()
pi = 4 * count / num_samples
print(pi)
sc0.stop()


def init_spark():
    spark = SparkSession.builder.appName("HelloWorld").getOrCreate()
    sc = spark.sparkContext
    return spark, sc


def main():
    spark, sc = init_spark()
    nums = sc.parallelize([1, 2, 3, 4])
    print(nums.map(lambda x: x*x).collect())


if __name__ == '__main__':
    main()
