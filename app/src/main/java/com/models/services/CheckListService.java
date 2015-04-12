package com.models.services;

import android.content.Context;

import com.models.CheckList;
import com.models.mappers.CheckListMapper;

import java.util.ArrayList;

/**
 * Created by Edgar on 11/04/2015.
 */
public class CheckListService {

    public CheckListService(Context context){
        checkListMapper = new CheckListMapper(context);
    }
    public CheckListService(){
    }

    public boolean addCheckList(CheckList checkList){
        long longAux = checkListMapper.insertCheckList(checkList);
        if(longAux > 0){
            return true;
        }else{
            return false;
        }
    }

    public void updateCheckList(CheckList checkList){
        checkListMapper.updateCheckList(checkList);
    }

    public ArrayList<CheckList> findAllCheckListByNoteId(int noteId){
        return checkListMapper.findAllCheckListByNoteId(noteId);
    }

    public void delete(CheckList checkList){
        checkListMapper.deleteCheckList(checkList);
    }

    private CheckListMapper checkListMapper;
}
