package com.whx.changecode.utils

class FileUtils {

    static List<File> getFileList(String path, String suffix) {
        List<File> res = new ArrayList<>()

        File dir = new File(path)

        if (dir.isDirectory()) {
            dir.eachFileRecurse { file ->
                if (file.name.endsWith(suffix)) {
                    res.add(file)
                }
            }
        }

        return res
    }
}