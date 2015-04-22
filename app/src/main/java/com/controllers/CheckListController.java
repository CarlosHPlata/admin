package com.controllers;

import android.content.Context;

import com.models.CheckList;
import com.models.services.CheckListService;

import java.util.ArrayList;

/**
 * Esta clase se encarga de administrar (agregar, borrar) la lista de tareas o checklist de una nota.
 * Created by Edgar on 11/04/2015.
 */
public class CheckListController {

    public CheckListController(Context context){
        checkListService = new CheckListService(context);
    }

    public boolean addCheckList(String description, int noteId){
        CheckList checkList = new CheckList();
        checkList.setDescription(description);
        checkList.setExtId(0);
        checkList.setChecked(false);
        checkList.setNoteId(noteId);
        checkList.setSyncFlag(false);

        boolean response = checkListService.addCheckList(checkList);
        return response;
    }

    public ArrayList<CheckList> findAllByNoteId(int noteId){
        return checkListService.findAllCheckListByNoteId(noteId);
    }

    public void updateCheckList(CheckList checkList){
        checkListService.updateCheckList(checkList);
    }

    public void deleteCheckList(CheckList checkList){
        checkListService.delete(checkList);
    }

    private CheckListService checkListService;
}
