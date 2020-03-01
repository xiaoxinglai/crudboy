package com.lxx.crudboy.action.base;

import com.intellij.ide.IdeView;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.psi.JavaDirectoryService;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.file.JavaDirectoryServiceImpl;
import com.lxx.crudboy.config.configurable.CrudBoyConfigurable;
import com.lxx.crudboy.config.configurable.CrudBoySettings;
import com.lxx.crudboy.util.MysqlJdbc;
import com.lxx.crudboy.util.PropertiesUtil;
import com.lxx.crudboy.util.sqlfilte.SqlParserFilter;
import org.picocontainer.PicoContainer;

import java.util.List;


/**
 * @ClassName BaseAnAction
 * @Author laixiaoxing
 * @Date 2020/2/2 上午12:23
 * @Description AnActionEvent 一些基本信息
 * @Version 1.0
 */
public abstract class BaseAnAction extends AnAction {

    private AnActionEvent anActionEvent;
    private DataContext dataContext;
    private Presentation presentation;
    private Module module;
    private IdeView view;
    private ModuleType moduleType;
    private Project project;
    private PsiDirectory psiDirectory;
    private DialogBuilder builder;
    private PsiFile file;
    private JavaDirectoryService javaDirectoryService;
    private MysqlJdbc mysqlJdbc;
    private PropertiesUtil properties = PropertiesUtil.getConfigProperties();
    private CrudBoySettings settings;

    public void init(AnActionEvent anActionEvent) {
        settings = ServiceManager.getService(CrudBoySettings.class);
        mysqlJdbc  = MysqlJdbc.getMysqlJdbc(settings);
        //java文件服务
        this.javaDirectoryService = new JavaDirectoryServiceImpl();
        //anAction
        this.anActionEvent = anActionEvent;
        IdeView ideView = (IdeView)anActionEvent.getRequiredData(LangDataKeys.IDE_VIEW);
        //选择的文件夹
        this.psiDirectory = ideView.getOrChooseDirectory();
        //选择的项目
        this.project = this.psiDirectory.getProject();
    }




    @Override
    public void update(AnActionEvent e) {
        try {
            this.presentation = e.getPresentation();
            this.onMenuUpade(e, (PsiFile)e.getData(DataKeys.PSI_FILE), ((IdeView)LangDataKeys.IDE_VIEW.getData(e.getDataContext())).getOrChooseDirectory());
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public void onMenuUpade(AnActionEvent e, PsiFile file, PsiDirectory dir) {
    }


    public void show() {
        this.presentation.setEnabled(true);
        this.presentation.setVisible(true);
    }

    public void hide() {
        this.presentation.setEnabled(false);
        this.presentation.setVisible(false);
    }


    public AnActionEvent getAnActionEvent() {
        return anActionEvent;
    }

    public DataContext getDataContext() {
        return dataContext;
    }

    public Presentation getPresentation() {
        return presentation;
    }

    public Module getModule() {
        return module;
    }

    public IdeView getView() {
        return view;
    }

    public ModuleType getModuleType() {
        return moduleType;
    }

    public Project getProject() {
        return project;
    }

    public PsiDirectory getPsiDirectory() {
        return psiDirectory;
    }

    public DialogBuilder getBuilder() {
        return builder;
    }

    public PsiFile getFile() {
        return file;
    }

    public JavaDirectoryService getJavaDirectoryService() {
        return javaDirectoryService;
    }

    public MysqlJdbc getMysqlJdbc() {
        return mysqlJdbc;
    }

    public PropertiesUtil getProperties() {
        return properties;
    }

    public CrudBoySettings getSettings() {
        return settings;
    }
}

