import React, { useEffect, useState } from 'react'
import classes from './managmentNewsPage.module.css'
import { Link, useLocation, useNavigate, useParams } from 'react-router-dom'

import * as newsService from '../../api/newsService'
import { toast } from 'react-toastify';

import NotFound from '../../components/NotFound/NotFound';
import Search from '../../components/Search/Search';
import Title from '../../components/Title/Title';
import Pagination from '@mui/material/Pagination';
import Button from '../../components/Button/Button';
import Popup from '../../components/Popup/Popup';
import NewsForm from '../../components/NewsForm/NewsForm';
import Controls from '../../components/Controls/Controls';

import EditOutlinedIcon from '@material-ui/icons/EditOutlined';
import CloseIcon from '@material-ui/icons/Close';

export function ManagmentNewsPage() {
  const [page, setPage] = useState(null);
  const { searchTerm } = useParams();

  const navigate = useNavigate();
  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const pageNumber = queryParams.get('page') || 1;
  const newsNumber = queryParams.get('news') || 8;

  const [openPopup, setOpenPopup] = useState(false);
  const [recordForEdit, setRecordForEdit] = useState(null)

  useEffect(() => {
      loadPage();
  }, [pageNumber, newsNumber, searchTerm]);

  const loadPage = async () => {
      try {
          const loadPage = searchTerm ? await newsService.getBySearchTerm(searchTerm, pageNumber - 1, newsNumber) : await newsService.getPageOfNews(pageNumber - 1, newsNumber);
          setPage(loadPage);
      } catch (error) {
          toast.error(error.message);
      }
  };

  // Without this, the page loads with error
  if (!page) {
      return <div>Loading...</div>;
  }

  const handlePageChange = (event, currentPage) => {
      event.preventDefault();
      navigate(`?page=${currentPage}&products=${newsNumber}`);
  };

  const openInPopup = item => {
      setRecordForEdit(item)
      setOpenPopup(true)
  };

  const addOrEdit = async (news, resetForm) => {
    try {
        const newsRequest = {
            id: news.id,
            title: news.title,
            content: news.content,
            newsImage: news.newsImage,
        };

        if (!news.id) {
            await newsService.createNews(newsRequest);
        } else {
            await newsService.updateNews(newsRequest);
        }

        toast.success("News published successfully.");
        resetForm();
        setRecordForEdit(null);
        setOpenPopup(false);
    } catch (error) {
        toast.error("Failed to publish product.");
    }
};


  const NewsNotFound = () => {
      if (page.data && page.data.content.length > 0) return;

      return searchTerm ? (
        <NotFound linkRoute="/managment/news" linkText="Show All" />
      ) : (
        <NotFound linkRoute="/managment" linkText="Back to Managment Dashboard!" />
      );
  };

  const deleteNews = async (news) => {
      const confirmed = window.confirm(`Delete News ${news.title}?`);
      if (!confirmed) return;

      await newsService.deleteById(news.id);
      toast.success(`"${news.title}" Has Been Removed From The Page!`);
  };

  return (
      <div className={classes.container}>
        <div className={classes.list}>
          <Title title="Manage News" margin="1rem auto" />
          <Search
            searchRoute="/managment/news/"
            defaultRoute="/managment/news"
            margin="1rem 0"
            placeholder="Search News"
          />
          <Button className={classes.add_product} text="Add News" onClick={() => setOpenPopup(true)} />
          <NewsNotFound />
          {page.data.content &&
            page.data.content.map(news => (
              <div key={news.id} className={classes.list_item}>
                <img src={news.newsImage ? `${news.newsImage}` : "/no_image_placeholder.svg"} alt={news.title} />
                {/*<Link to={'/news/' + news.title}>{news.title}</Link>*/}
                <Link to={'/'}>{news.title}</Link>
                <p>{news.content}</p>
                <div className={classes.actions}>
                    <Controls.ActionButton
                        color="primary"
                        onClick={() => { openInPopup(news) }}>
                            <EditOutlinedIcon fontSize="small" />
                    </Controls.ActionButton>
                    <Controls.ActionButton
                        color="secondary"
                        onClick={() => { deleteNews(news) }}>
                            <CloseIcon fontSize="small" />
                    </Controls.ActionButton>
                </div>
              </div>
          ))}
                        <div className={classes.pagination_container}>
                        <Pagination
                            onChange={handlePageChange}
                            count={page.data.totalPages}
                            variant="outlined"
                            shape="rounded"
                            sx={{
                                '& .MuiPaginationItem-root': {
                                    color: 'white',
                                    backgroundColor: '#000000cc'
                                },
                                '& .MuiPaginationItem-root:hover': {
                                    color: 'black',
                                    backgroundColor: 'wheat',
                                },
                                '& .MuiPaginationItem-root.Mui-selected': {
                                    color: '111',
                                    backgroundColor: '#00000060'
                                },
                                '& .MuiPaginationItem-root.Mui-selected:hover': {
                                    color: 'black',
                                    backgroundColor: 'wheat',
                                }
                            }}
                        />
                        </div>
        </div>
        <Popup
          title="News Details"
          openPopup={openPopup}
          setOpenPopup={setOpenPopup}
        >
            <NewsForm
                recordForEdit={recordForEdit}
                addOrEdit={addOrEdit}
            />
        </Popup>
      </div>
  );
}