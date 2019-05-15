# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

add_library(result SHARED src/main/cpp/result.cpp)

find_library(log-lib log)
find_library(android-lib android)

target_link_libraries(result
                       ${log-lib}
                       ${android-lib})