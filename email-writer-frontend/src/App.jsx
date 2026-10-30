
import { Box, Container, Typography, TextField, FormControl, InputLabel, MenuItem, Select, Button,  CircularProgress } from '@mui/material';
import axios from 'axios';
import './App.css'
import { useState } from 'react'

function App() {
const [emailContent, setEmailContent] = useState('');
const [tone, setTone] = useState('');
const [generatedReply, setGeneratedReply] = useState('');
const [loading, setLoading] = useState(false);

const handleSubmit = async () => {
  setLoading(true);

  try {
    console.log("Sending request...");
    console.log("Email Content:", emailContent);
    console.log("Tone:", tone);

    const response = await axios.post(
      "http://localhost:8081/api/email/generate",
      {
        emailContent,
        tone
      }
    );

    console.log("API Response:", response.data);

    setGeneratedReply(
      typeof response.data === 'string'
        ? response.data
        : JSON.stringify(response.data)
    );

  } catch (error) {
    console.error("Error generating reply:", error);
    setGeneratedReply("Failed to generate reply. Check the console.");
  } finally {
    setLoading(false);
  }
};

  return (
    <Container maxWidth="md" sx={{py:4}}>
      <Typography variant='h3' component="h1" gutterBottom>
        Email Reply Generator 
      </Typography>

      <Box sx={{mx: 3}}>
        <TextField
          fullWidth
          multiline
          rows={6}
          variant='outlined'
          label="Original Email Content"
          value={emailContent || ''}
          onChange={(e) => setEmailContent(e.target.value)}
          sx={{mb:2}}
        />

        <FormControl fullWidth sx={{mb:2}}>
          <InputLabel>Tone(optional)</InputLabel>
          <Select
            
            value={tone || ''}
            label="Tone(optional)"
            onChange={(e) => setTone(e.target.value)}
          >
            <MenuItem value="">None</MenuItem>
            <MenuItem value="professional">Professional</MenuItem>
            <MenuItem value="casual">Casual</MenuItem>
            <MenuItem value="friendly">Friendly</MenuItem>
          </Select>
        </FormControl>

        <Button variant="contained" 
        sx={{mb:2}}
        onClick={handleSubmit}
        disabled = {!emailContent || loading}>
          {loading ? <CircularProgress size={24}/> : "Generate Reply"}
        
        </Button>

      </Box>

      <Box sx={{mx: 3}}>
        <TextField
          fullWidth
          multiline
          rows={6}
          variant='outlined'
          value={generatedReply || ''}
          slotProps={{readOnly: true}}
          sx={{mb:2}}
        />

        <Button variant="outlined" 
        onClick={() => navigator.clipboard.write(generatedReply)}>
          Copy to Clipboard
        </Button>


        </Box>

    </Container>
  )
}

export default App
