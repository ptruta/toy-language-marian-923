package controller.filetable;

import java.util.HashMap;

public interface IFileTable<Str, Reader> {
    public void add(Str filename, Reader buffer);

    public Reader get(Str filename);

    public HashMap<Str, Reader> getAll();
}
