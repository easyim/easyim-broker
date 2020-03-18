package com.broker.base;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

public interface IStorage {
     void setKeyValue(String obj, String key, String value) throws StorageException;
     void setKeyValue(String obj, Map<String, String> keyValues) throws StorageException;
     @NotNull
     String getValue(String obj, String key) throws StorageException;
     Map<String, String> getValue(String obj, Set<String> keys) throws StorageException;
     Map<String, String> getAllKeyValues(String obj) throws StorageException;
     void remove(String obj, String key) throws StorageException;
}
