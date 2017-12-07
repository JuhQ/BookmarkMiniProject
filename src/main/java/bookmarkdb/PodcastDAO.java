
package bookmarkdb;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import bookmarkmodels.Podcast;

/**
 *  Class for accessing database table for bookmarks of type 'Podcast'.
 */

public class PodcastDAO implements AbstractDAO<Podcast, Integer>{
    
    private final AbstractDatabase database;

    public PodcastDAO(AbstractDatabase database) {
        this.database = database;
    }
    
    

    @Override
    public Podcast create(Podcast podcast) throws SQLException {
        
        List<Podcast> allPodcasts = this.findAll();
        for (Podcast existingPod : allPodcasts) {
            if (podcast.getAuthor().equals(existingPod.getAuthor())
                    && podcast.getName().equals(existingPod.getName())
                    && podcast.getTitle().equals(existingPod.getTitle())) {
                return null;
            }
        }
        database.update("INSERT INTO Podcast(name, title, author, url) VALUES (?, ?, ?, ?)", podcast.getName(), podcast.getTitle(), podcast.getAuthor(), podcast.getUrl());
        
        return podcast;
    }

    @Override
    public Podcast findOne(Podcast podcast) throws SQLException {
        Map<String, List<String>> results = database.query("SELECT * FROM Podcast WHERE author=? AND title=?", podcast.getAuthor(), podcast.getTitle());
        
        if (results.get("title").size() > 0) {
            Podcast found = new Podcast();
            for (String col : results.keySet()) {
                if (col.equals("name")) {
                    found.setName(results.get(col).get(0));
                }
                if (col.equals("author")) {
                    found.setAuthor(results.get(col).get(0));
                }
                if (col.equals("title")) {
                    found.setTitle(results.get(col).get(0));
                }
                if (col.equals("url")) {
                    found.setUrl(results.get(col).get(0));
                }
            }
            return found;
        }
        return null;
    }

    @Override
    public List<Podcast> findAll() throws SQLException {
        List<Podcast> podcasts = new ArrayList<>();
        Map<String, List<String>> results = database.query("SELECT * FROM Podcast");
        
        for (int i = 0; i < results.get("title").size(); i++) {
            Podcast podcast = new Podcast();
            for (String col : results.keySet()) {
                if (col.equals("name")) {
                    podcast.setName(results.get(col).get(i));
                } else if (col.equals("author")) {
                    podcast.setAuthor(results.get(col).get(i));
                } else if (col.equals("title")) {
                    podcast.setTitle(results.get(col).get(i));
                } else if (col.equals("url")) {
                    podcast.setUrl(results.get(col).get(i));
                }
            }
            podcasts.add(podcast);
        }
        return podcasts;
    }

    @Override
    public void update(Podcast oldPod, Podcast newPod) throws SQLException {
        Podcast old = findOne(oldPod);
        if (old.getAuthor() != null) {
            database.update("UPDATE Podcast SET name=?, author=?, title=?, url=? WHERE author=? AND title=?", newPod.getName(), newPod.getAuthor(), newPod.getTitle(), newPod.getUrl(), old.getAuthor(), old.getTitle());
        }
    }

    @Override
    public boolean delete(Podcast podcast) throws SQLException {
        int deleted =
                database.update("DELETE FROM Podcast WHERE author=? AND title=? AND name=?",
                        podcast.getAuthor(), podcast.getTitle(), podcast.getName());
        return deleted == 1;
    }

    @Override
    public List<Podcast> findAllWithKeyword(String s) throws SQLException {
        List<Podcast> podcasts = new ArrayList<>();
        String keyword = "\'%" + s + "\'%";
        Map<String, List<String>> results = database.query("SELECT * FROM Podcast"
                + " WHERE author LIKE ? OR title LIKE ? OR name LIKE ? OR url LIKE ?"
                , keyword, keyword, keyword, keyword);
        
        for (int i = 0; i < results.get("title").size(); i++) {
            Podcast podcast = new Podcast();
            for (String col : results.keySet()) {
                if (col.equals("name")) {
                    podcast.setName(results.get(col).get(i));
                } else if (col.equals("author")) {
                    podcast.setAuthor(results.get(col).get(i));
                } else if (col.equals("title")) {
                    podcast.setTitle(results.get(col).get(i));
                } else if (col.equals("url")) {
                    podcast.setUrl(results.get(col).get(i));
                }
            }
            podcasts.add(podcast);
        }
        return podcasts;
    }

    @Override
    public void marksAsChecked(Podcast t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
