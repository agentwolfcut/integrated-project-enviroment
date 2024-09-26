package dev.backendintegratedproject.util;

import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class ListMapper<E,T> {
    private static final ListMapper LIST_MAPPER = new ListMapper();

    private ListMapper(){};
    public static ListMapper getListMapper(){
        return LIST_MAPPER;
    }
    public List<T> mapList(List<E> srcList, Class<T> targetClass, ModelMapper modelMapper){
        return srcList.stream().map((srcItem) -> modelMapper.map(srcItem, targetClass)).collect(Collectors.toList());
    }
}
